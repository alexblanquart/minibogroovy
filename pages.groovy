@Grab('com.github.spullara.mustache.java:compiler:0.9.1')
@Grab('org.pegdown:pegdown:1.6.0')

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import groovy.json.*
import org.pegdown.PegDownProcessor

// parse meta infos
def metasByName = [:]
def slurper = new JsonSlurper()
new File("./posts/meta").eachFile {  metasByName[it.name.replace(".json", "")] = slurper.parseText(it.text) }

// compute contents
def contentsByName = [:]
new File("./posts/content").eachFile { contentsByName[it.name.replace(".md", "")] = it.text }

// combine meta and content to create posts
def posts = metasByName.collect { name, meta ->
    def content = contentsByName[name]
    meta + [content:content, name:name]
}

// sort posts
posts.sort{ Date.parse('dd-MM-yyyy', it.date) }
posts = posts.reverse(true)

// compute tags
def duplicatedTags = posts.collect{ it.tags }.flatten() 
def tagsByCount = duplicatedTags.countBy{ it }
def allTags = duplicatedTags.unique()
println "*********\n"
println "List of all tags from all posts : $allTags\n"
println "*********\n"

// init mustache engine with our layouts
def mf = new DefaultMustacheFactory("layouts")
def postMustache = mf.compile("post.html")
def postsMustache = mf.compile("posts.html")
def aboutMustache = mf.compile("about.html")
/*def ideaMustache = mf.compile("idea.html")
def ideasMustache = mf.compile("ideas.html")
def indexMustache = mf.compile("index.html")*/

// create post pages
def pdp = new PegDownProcessor()
Locale.setDefault(Locale.FRENCH)
def recentPosts = posts.take(5)
posts.each { post ->
    post.content = pdp.markdownToHtml(post.content)
    post.date = Date.parse('dd-MM-yyyy', post.date).format('d MMMM yyyy')
    new File("./static/pages", post.name+".html").withWriter("utf-8") { writer ->
        postMustache.execute(writer, 
            post + [recentPosts:recentPosts, summary:post.content.take(100), allTags:tagsByCount.entrySet()]
        ).flush();        
    }
}

// create blog page
new File("./static/pages", "blog.html").withWriter("utf-8") { writer ->
    postsMustache.execute(writer, [posts:posts.findAll{it.category == "blog"}]).flush();        
}

// create tag pages
allTags.each { tag ->
    def tagPosts = posts.findAll{tag in it.tags}
    new File("./static/pages", tag+".html").withWriter("utf-8") { writer ->
        // posts list not empty as tag could not exist alone
        postsMustache.execute(writer, [category: tagPosts[0].category, posts:tagPosts]).flush();        
    }
}

// create about page
new File("./static/pages", "about.html").withWriter("utf-8") { writer ->
    aboutMustache.execute(writer, null).flush();        
}

// create search file
new File("static", "posts.json").withWriter("utf-8") { writer ->
    writer.write(JsonOutput.toJson(posts.collect{ ["title":it.title, "url":"/pages/"+it.name+".html"] }))
}
