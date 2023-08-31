javac Main.java -d out
cp cycle*.txt out
cd out
java Main
cd ..
rm -rf out