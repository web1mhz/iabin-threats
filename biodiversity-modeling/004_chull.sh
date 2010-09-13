g.mapset mapset=make_clip -c
g.region sa@PERMANENT
buffer_distance=2 # in degree
count=1
total=`ls | grep ^[0-9].* | wc -l` > info.txt
ls | grep ^[0-9].* | wc -l > info.txt
date >> info.txt

for i in `ls | grep ^[0-9].*`
do
  cat $i/training/species.csv | awk -F',' 'NR > 1 {print $2,$3}' | v.in.ascii out=occ_${i} fs=" " --o
  v.hull in=occ_$i out=occ_$i\_hull --o
  v.buffer in=occ_$i\_hull out=occ_$i\_hull_buffer distance=$buffer_distance type=area --o
  v.out.ogr input=occ_${i}_hull_buffer dsn=${i}/proj/mask.shp layer=1 format=ESRI_Shapefile type=area

  gunzip ${i}/proj/$i
  cp ${i}/proj/$i ${i}/proj/$i.asc
  rm ${i}/proj/$i
  gdalwarp -cutline $i/proj/mask.shp ${i}/proj/$i.asc ${i}/proj/${i}_buffer.asc
  gzip ${i}/proj/$i.asc ${i}/proj/${i}_buffer.asc

  echo "finished $count of $total!!"
  count=$(( count+1 ))
done

date >> info.txt


