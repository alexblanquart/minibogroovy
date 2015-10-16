@Grab('com.github.spullara.mustache.java:compiler:0.9.1')
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import groovy.json.*

// init mustache engine with our layouts
def mf = new DefaultMustacheFactory("layouts");
def mustache = mf.compile("post.html")
def slurper = new JsonSlurper()

// renew generated posts html
new File("./static/posts").deleteDir()
new File("./static/posts").mkdirs()

// generate posts html
def allMetaInfos = []
new File("./posts/meta").eachFile { meta ->
    if (meta.name.endsWith("json")){
        def metaInfo = slurper.parseText(meta.text)
        allMetaInfos << metaInfo        
    }        
}
def allTags = []
allMetaInfos.collect{ it.tags.each{allTags << it} }
println allTags.countBy({it})

/*new File("./posts/meta").eachFile { meta ->
    if (meta.name.endsWith("json")){
        new File("./static/posts", meta.name.replace("json", "html")).withWriter("utf-8") { writer ->
            def metaInfo = slurper.parseText(meta.text)
            allMetaInfos << metaInfo
            mustache.execute(writer, slurper.parseText(metaInfo)).flush();        
        }
    }        
}*/