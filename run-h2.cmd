docker run -d -p 1521:1521 -p 81:81 -v /path/to/local/data_dir:/opt/h2-data -e H2_OPTIONS=-ifNotExists --name=my-h2 rkaehdaos/h2