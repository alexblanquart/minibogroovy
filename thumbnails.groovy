@Grab('net.coobird:thumbnailator:0.4.8')

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.name.Rename

// create thumbnails
new File("static/images/posts").deleteDir()
new File("static/images/posts").mkdirs()
Thumbnails.of(new File("images").listFiles())
    .width(500)
    .toFiles(new File("static/images/posts"), Rename.NO_CHANGE);