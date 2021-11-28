######### Detecção de objetos Picamera usando o classificador Tensorflow ##########
#
# Descrição:
# Este programa usa um classificador TensorFlow para realizar a detecção de objetos.
# Carrega o classificador e usa-o para realizar a detecção de objetos em um feed do Picamera.
# Ele desenha caixas e pontuações em torno dos objetos de interesse em cada quadro de
# a Picamera. Também pode ser usado com uma webcam adicionando "--usbcam"
# ao executar este script a partir do terminal.

## Parte do código foi usado do exemplo do Google em
## https://github.com/tensorflow/models/blob/master/research/object_detection/object_detection_tutorial.ipynb

## e alguns são usados do exemplo de Dat Tran em
## https://github.com/datitran/object_detector_app/blob/master/object_detection_app.py

# Importa pacotes
import os
import cv2
import numpy as np
from picamera.array import PiRGBArray
from picamera import PiCamera
import tensorflow as tf
import argparse
import sys
import time

# Configura constantes de câmera
IM_WIDTH = 1280
IM_HEIGHT = 720
#IM_WIDTH = 640    Use resolução menor para
#IM_HEIGHT = 480   taxa de quadros ligeiramente mais rápida

# Selecione o tipo de câmera (se o usuário digitar --usbcam ao chamar este script,
# será usada uma webcam USB)
camera_type = 'picamera'
parser = argparse.ArgumentParser()
parser.add_argument('--usbcam', help='Use a USB webcam instead of picamera',
                    action='store_true')
args = parser.parse_args()
if args.usbcam:
    camera_type = 'usb'

# This is needed since the working directory is the object_detection folder.
sys.path.append('..')

# Importa utilitários
from utils import label_map_util
from utils import visualization_utils as vis_util

# Nome do diretório que contém o módulo de detecção de objeto que é usado
MODEL_NAME = 'ssdlite_mobilenet_v2_coco_2018_05_09'

# Pega o caminho para o diretório atual
CWD_PATH = os.getcwd()

# Caminho para o arquivo .pb do gráfico de detecção de congelados, que contém o modelo usado
# para detecção de objetos.
PATH_TO_CKPT = os.path.join(CWD_PATH,MODEL_NAME,'frozen_inference_graph.pb')

# Caminho para arquivo de mapa de rótulo
PATH_TO_LABELS = os.path.join(CWD_PATH,'data','mscoco_label_map.pbtxt')

# Número de classes que o detector de objetos pode identificar
NUM_CLASSES = 90

## Carregue o mapa de rótulos.
# Label mapeia índices de mapas para nomes de categorias, de modo que quando a convolução
# a rede prevê `5`, sabemos que corresponde a` avião`.
# Aqui usamos funções utilitárias internas, mas qualquer coisa que retorne um
# mapeamento de inteiros de dicionário para rótulos de string apropriados seria bom
label_map = label_map_util.load_labelmap(PATH_TO_LABELS)
categories = label_map_util.convert_label_map_to_categories(label_map, max_num_classes=NUM_CLASSES, use_display_name=True)
category_index = label_map_util.create_category_index(categories)

# Carregue o modelo Tensorflow na memória.
detection_graph = tf.Graph()
with detection_graph.as_default():
    od_graph_def = tf.compat.v1.GraphDef()
    with tf.compat.v2.io.gfile.GFile(PATH_TO_CKPT, 'rb') as fid:
        serialized_graph = fid.read()
        od_graph_def.ParseFromString(serialized_graph)
        tf.import_graph_def(od_graph_def, name='')

    sess = tf.compat.v1.Session(graph=detection_graph)


# Definir tensores de entrada e saída (ou seja, dados) para o classificador de detecção de objeto

# O tensor de entrada é a imagem
image_tensor = detection_graph.get_tensor_by_name('image_tensor:0')

# Os tensores de saída são as caixas de detecção, pontuações e classes
# Cada caixa representa uma parte da imagem onde um determinado objeto foi detectado
detection_boxes = detection_graph.get_tensor_by_name('detection_boxes:0')

# Cada pontuação representa o nível de confiança de cada um dos objetos.
# A pontuação é mostrada na imagem do resultado, junto com o rótulo da classe.
detection_scores = detection_graph.get_tensor_by_name('detection_scores:0')
detection_classes = detection_graph.get_tensor_by_name('detection_classes:0')

# Número de objetos detectados
num_detections = detection_graph.get_tensor_by_name('num_detections:0')

# Inicializa o cálculo da taxa de quadros
frame_rate_calc = 1
freq = cv2.getTickFrequency()
font = cv2.FONT_HERSHEY_SIMPLEX

# Inicialize a câmera e execute a detecção de objetos.
# A câmera deve ser configurada e usada de forma diferente, dependendo se for um
# Picamera ou webcam USB.

# loop de detecção duas vezes, e fez um funcionar para Picamera e o outro funcionar
# para USB.

### Picamera ###
if camera_type == 'picamera':
    # Inicialize o Picamera e pegue a referência para a captura bruta
    camera = PiCamera()
    camera.resolution = (IM_WIDTH,IM_HEIGHT)
    camera.framerate = 10
    rawCapture = PiRGBArray(camera, size=(IM_WIDTH,IM_HEIGHT))
    rawCapture.truncate(0)

    for frame1 in camera.capture_continuous(rawCapture, format="bgr",use_video_port=True):

        t1 = cv2.getTickCount()
        
        # Adquira o quadro e expanda as dimensões do quadro para ter a forma: [1, Nenhum, Nenhum, 3]
        # ou seja, uma matriz de coluna única, em que cada item na coluna tem o valor RGB de pixel
        frame = np.copy(frame1.array)
        frame.setflags(write=1)
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        frame_expanded = np.expand_dims(frame_rgb, axis=0)

        # Execute a detecção real executando o modelo com a imagem como entrada
        (boxes, scores, classes, num) = sess.run(
            [detection_boxes, detection_scores, detection_classes, num_detections],
            feed_dict={image_tensor: frame_expanded})

        # Desenhe os resultados da detecção (também conhecido como 'visualizar os resultados')
        vis_util.visualize_boxes_and_labels_on_image_array(
            frame,
            np.squeeze(boxes),
            np.squeeze(classes).astype(np.int32),
            np.squeeze(scores),
            category_index,
            use_normalized_coordinates=True,
            line_thickness=8,
            min_score_thresh=0.40)

        cv2.putText(frame,"FPS: {0:.2f}".format(frame_rate_calc),(30,50),font,1,(255,255,0),2,cv2.LINE_AA)


        ## se a classe for de cachorro ou gato (para testes), ele chama os script de liberação de racao
        if(int(classes[0][0])==17) or (int(classes[0][0])==18):      
            exec(open("/home/pi/fechaRacao.py").read())
            time.sleep(10)
            exec(open("/home/pi/liberaRacao.py").read())
            
        # Todos os resultados foram desenhados no quadro, então é hora de exibi-lo.
        cv2.imshow('Object detector', frame)

        t2 = cv2.getTickCount()
        time1 = (t2-t1)/freq
        frame_rate_calc = 1/time1

        # Pressione 'q' para sair
        if cv2.waitKey(1) == ord('q'):
            break

        rawCapture.truncate(0)

    camera.close()

### USB webcam ###
elif camera_type == 'usb':
    # Inicializar feed de webcam USB
    camera = cv2.VideoCapture(0)
    ret = camera.set(3,IM_WIDTH)
    ret = camera.set(4,IM_HEIGHT)

    while(True):

        t1 = cv2.getTickCount()

        # Adquira o quadro e expanda as dimensões do quadro para ter a forma: [1, none, none, 3]
        # ou seja, uma matriz de coluna única, em que cada item na coluna tem o valor RGB de pixel
        ret, frame = camera.read()
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        frame_expanded = np.expand_dims(frame_rgb, axis=0)

        # Execute a detecção real executando o modelo com a imagem como entrada
        (boxes, scores, classes, num) = sess.run(
            [detection_boxes, detection_scores, detection_classes, num_detections],
            feed_dict={image_tensor: frame_expanded})

        # Desenhe os resultados da detecção (também conhecido como 'visualizar os resultados')
        vis_util.visualize_boxes_and_labels_on_image_array(
            frame,
            np.squeeze(boxes),
            np.squeeze(classes).astype(np.int32),
            np.squeeze(scores),
            category_index,
            use_normalized_coordinates=True,
            line_thickness=8,
            min_score_thresh=0.85)

        cv2.putText(frame,"FPS: {0:.2f}".format(frame_rate_calc),(30,50),font,1,(255,255,0),2,cv2.LINE_AA)
        
        # Todos os resultados foram desenhados no quadro, então é hora de exibi-lo.
        cv2.imshow('Object detector', frame)

        t2 = cv2.getTickCount()
        time1 = (t2-t1)/freq
        frame_rate_calc = 1/time1

        # Pressione 'q' para sair
        if cv2.waitKey(1) == ord('q'):
            break

    camera.release()

cv2.destroyAllWindows()

