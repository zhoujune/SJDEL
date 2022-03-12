antlr  := java -jar $(PWD)/lib/antlr-4.7.1-complete.jar -no-listener -visitor
javac  := javac -cp $(PWD)/lib/antlr-4.7.1-complete.jar

.PHONY: clean

readFormula: build/formula/
	echo '#!/bin/bash' > ./readFormula
	echo 'java -cp "./lib/*:./build/" formula.ReadFormula $$@' >> ./readFormula
	chmod +x ./readFormula

build/formula/: build/parserSrc/
	$(javac) src/*.java build/parserSrc/*.java -d build/

build/parserSrc/: src/Formula.g4
	cd src; $(antlr) -package formula Formula.g4 -o ../build/parserSrc

clean:
	rm -rf ./build
	rm -f ./readFormula

