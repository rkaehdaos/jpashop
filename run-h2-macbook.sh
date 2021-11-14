docker run -d \
       -p 1521:1521 -p 81:81 \
       -v /Users/ahn/h2-data:/opt/h2-data \
       -e H2_OPTIONS=-ifNotExists \
       --name=my-h2 \
        rkaehdaos/h2