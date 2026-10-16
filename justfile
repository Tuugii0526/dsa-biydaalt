default: run

# compile biydaalt (+ whatever it depends on) into bin/
compile:
    mkdir -p bin
    javac -encoding UTF-8 -sourcepath src -d bin src/biydaalt/*.java

# compile, then run Registration.main (from src/ so relative file paths work)
run: compile
    cd src && java -cp ../bin biydaalt.Registration
