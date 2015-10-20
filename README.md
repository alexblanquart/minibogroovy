# What is it ?

Jekyll website with facilities for non tech to use it.

# What is missing ?

We aim at getting:
- DIY logo
- social links to facebook, pinterest, and social shares to facebook, twitter, pinterest, instagram, google+
- rename images for better coherence
- domain name
- disqus integration
- categories ?
- posts by date
- responsive
- google analytics
- missing images for posts, reread all posts
- tutorial for non tech on how to use and integrate new content
- software or tools to improve the content integration
- search over other things than posts
- inform readers when new content (post for example) is ready : newsletter? facebook?
- make products, tutorials, tags collections?
- image displayed as exceprt ?!
- SEO
- sitemap
- rewrite images from original to compressed nothing more
- current page with underline on navigation
- infinite scrolling
- research
- about : present all my products
- missing images ?
- cache for generating images/pages if present or to renew ? and gain in generation time

# Bash commands which can be useful

$ find . -type f -name "*.md" -print | xargs sed -i -e 's/{{ site.posts_images }}/{{ site.baseurl }}\/{{ site.posts_images }}/g'
$ sips --resampleWidth 340 *.jpg *.png
