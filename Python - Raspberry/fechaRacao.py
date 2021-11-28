#!/usr/bin/python
import sys
import time
import RPi.GPIO as GPIO
 
GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)

aMotorPins = [11, 12, 13, 15]
 
# Defina todos os pinos como saída
for pin in aMotorPins:
	GPIO.setup(pin,GPIO.OUT)
	GPIO.output(pin, False)

aSequence = [
	[1,0,0,1],
	[1,0,0,0],
	[1,1,0,0],
	[0,1,0,0],
	[0,1,1,0],
	[0,0,1,0],
	[0,0,1,1],
	[0,0,0,1]
]
        
iNumSteps = len(aSequence)

# sys.argv[3] == "cw":
iDirection = 1 #-1 ABRE, 1 FECHA
#else:
#	iDirection = -1

fWaitTime = int(1) / float(1000)

iDeg = int(int(90) * 11.377777777777)

iSeqPos = 0
# Se o quarto argumento estiver presente, significa que o motor deve partir em um
# posição específica da lista aSequence
if len(sys.argv) > 4:
	iSeqPos = int(sys.argv[4])

# 1024 steps é 90 graus
# 4096 steps é 360 graus

for step in range(0,iDeg):

	for iPin in range(0, 4):
		iRealPin = aMotorPins[iPin]
		if aSequence[iSeqPos][iPin] != 0:
			GPIO.output(iRealPin, True)
		else:
			GPIO.output(iRealPin, False)
 
	iSeqPos += iDirection
 
	if (iSeqPos >= iNumSteps):
		iSeqPos = 0
	if (iSeqPos < 0):
		iSeqPos = iNumSteps + iDirection
 
	# Tempo para aguardar entre passos
	time.sleep(fWaitTime)

for pin in aMotorPins:
	GPIO.output(pin, False)

# Imprime a posição da lista aSequence que deveria ser o
# próxima posição, se o loop anterior não foi encerrado
# Precisa capturar esta saída ao executar a partir de outro script

print (iSeqPos)