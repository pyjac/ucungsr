rm marf.Storage.TrainingSet.*

java -jar "dist/identificadorLPC.jar" -train waves/gustavo_1.wav 1
java -jar "dist/identificadorLPC.jar" -train waves/jesica_1.wav 2
java -jar "dist/identificadorLPC.jar" -train waves/MTLS_Sr19.wav 3

java -jar "dist/identificadorLPC.jar" -ident waves/gustavo_6.wav 
