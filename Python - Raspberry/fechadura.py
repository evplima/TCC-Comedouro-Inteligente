#Importe de todos os recursos necessários para o código.
import RPi.GPIO as GPIO
from time import sleep

#Se o código for interrompido enquanto o solenóide estiver ativo, ele permanecerá ativo
# Isso pode produzir um aviso se o código for reiniciado e encontrar o PIN GPIO, que ele define como não ativo na próxima linha, ainda está ativo
# da vez anterior em que o código foi executado. Essa linha evita que apareça a sintaxe de aviso que, se ocorresse, interromperia a execução do código.
GPIO.setwarnings(False)

#Isso significa que nos referiremos aos pinos GPIO
# pelo número diretamente após a palavra GPIO. Um bom recurso de pinagem pode ser encontrado aqui https://pinout.xyz/
GPIO.setmode(GPIO.BCM)

# Isso configura o pino 16 (GPIO 23) como um pino de saída
GPIO.setup(23, GPIO.OUT)

#while (True):    

    
#Liga o relé. Traz tensão para Min GPIO pode produzir ~ 0V.
GPIO.output(23, 0)
#aguarda 1 seg
sleep(60)
#Isso desliga o relé. Traz tensão para GPIO máx. Pode produzir ~ 3,3 V
GPIO.output(23, 1)
#aguarda 1 seg
#sleep(1)