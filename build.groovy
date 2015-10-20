@Grab('com.github.spullara.mustache.java:compiler:0.9.1')
@Grab('org.pegdown:pegdown:1.6.0')

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import groovy.json.*
import org.pegdown.PegDownProcessor

// init mustache engine with our layouts
def mf = new DefaultMustacheFactory("layouts")
def postMustache = mf.compile("post.html")
def aboutMustache = mf.compile("about.html")
def postsMustache = mf.compile("posts.html")
def indexMustache = mf.compile("index.html")
def slurper = new JsonSlurper()

// compute all tags found inside all posts
def allMetaInfos = []
new File("./posts/meta").eachFile { meta ->
    if (meta.name.endsWith("json")){
        def metaInfo = slurper.parseText(meta.text)
        allMetaInfos << metaInfo        
    }        
}
def allTagsList = []
allMetaInfos.collect{ it.tags.each{allTagsList << it} }
def tagsByCount = allTagsList.countBy({it})
def allTagsSet = allTagsList as Set 

// compute posts
def pdp = new PegDownProcessor()
def allPosts = []
new File("./posts/meta").eachFile { meta ->
    if (meta.name.endsWith("json")){
        def post = slurper.parseText(meta.text)
        post.allTags = allTagsSet
        
        // convert to html
        post.content = pdp.markdownToHtml(new File("./posts/content", meta.name.replace("json", "md")).text)
        // remove any html tags for summary
        post.summary = post.content.replaceAll("<(.|\n)*?>", "") 
        post.summary = post.summary[0..Math.min(100, post.summary.length()-1)]
        
        post.html =  meta.name.tokenize(".").first()+".html"
        post.url = "/pages/"+post.html
        post.date = Date.parse('dd-MM-yyyy', post.date) // get in Date form
        allPosts << post
    }        
}

// sort posts by date : the most recent on the top
Locale.setDefault(Locale.FRENCH)
allPosts.sort{it.date}
allPosts = allPosts.reverse(true)
def recentPosts = allPosts[0..4]
allPosts.each { post ->
    post.date = post.date.format("d MMMM yyyy") // pass again in string form for french
    post.recentPosts = recentPosts
}

// renew pages
new File("./static/pages").deleteDir()
new File("./static/pages").mkdirs()

// generate each post page
def blogPosts = allPosts.findAll{it.category == "blog"}
blogPosts.each { post ->
    post.recentPosts = blogPosts[0..4]
    new File("./static/pages", post.html).withWriter("utf-8") { writer ->
        postMustache.execute(writer, post).flush();        
    }
}
new File("./static/pages", "blog.html").withWriter("utf-8") { writer ->
    postsMustache.execute(writer, [category: "blog", posts:blogPosts]).flush();        
}

// generate each idea page
def ideaPosts = allPosts.findAll{it.category == "idea"}
ideaPosts.each { post ->
    post.recentPosts = ideaPosts[0]
    new File("./static/pages", post.html).withWriter("utf-8") { writer ->
        postMustache.execute(writer, post).flush();        
    }
}
new File("./static/pages", "ideas.html").withWriter("utf-8") { writer ->
    postsMustache.execute(writer, [category: "idea", posts:ideaPosts]).flush();        
}

// generate each workshop page
def workshopPosts = allPosts.findAll{it.category == "workshop"}
workshopPosts.each { post ->
    post.recentPosts = workshopPosts[0]
    new File("./static/pages", post.html).withWriter("utf-8") { writer ->
        postMustache.execute(writer, post).flush();        
    }
}
new File("./static/pages", "workshops.html").withWriter("utf-8") { writer ->
    postsMustache.execute(writer, [category: "workshop", posts:workshopPosts]).flush();        
}

// generate each tag page
allTagsSet.each { tag ->
    def tagPosts = allPosts.findAll{tag in it.tags}
    new File("./static/pages", tag+".html").withWriter("utf-8") { writer ->
        postsMustache.execute(writer, [category: tagPosts[0].category, posts:tagPosts]).flush();        
    }
}

// generate about page
new File("./static/pages", "about.html").withWriter("utf-8") { writer ->
    aboutMustache.execute(writer, null).flush();        
}

// generate index page
new File("./static/pages", "index.html").withWriter("utf-8") { writer ->
    indexMustache.execute(writer, [recentBlog:blogPosts[0..10]]).flush();        
}