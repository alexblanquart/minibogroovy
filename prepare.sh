#/bin/sh

cd images/posts/original
# resizing to same width
mogrify -resize 340 -path ../samewidth *.jpg
# thumbnailing
mogrify -resize 200x200 -path ../thumbnails *.jpg

