rm marf.Storage.TrainingSet.*

java -jar "dist/ucungsr.jar" -train waves/gustavo_1.wav 1
java -jar "dist/ucungsr.jar" -train waves/jesica_1.wav 2

java -jar "dist/ucungsr.jar" -ident waves/gustavo_6.wav 
