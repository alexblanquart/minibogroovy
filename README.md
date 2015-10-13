# What is it ?

Jekyll website with facilities for non tech to use it.

# What is missing ?

We aim at getting:
- DIY logo
- social links to facebook, pinterest, and social shares to facebook, twitter, pinterest, instagram, google+
- rename images for better coherence
- better visual: fonts, color, etc. (depoisdosquinze.com, chezsusan.ch)
- domain name
- disqus integration
- links between posts and other data
- products description
- categories ?
- index page with news, products and tutorials
- posts by date
- responsive
- infinite scroll for products
- google analytics
- missing images for posts, reread all posts
- tutorial for non tech on how to use and integrate new content
- software or tools to improve the content integration
- search over other things than posts
- inform readers when new content (post for example) is ready : newsletter? facebook?
- make products, tutorials, tags collections?
- image displayed as exceprt ?!
- SEO
- affix aside blog
- sitemap

# Bash commands which can be useful

$ find . -type f -name "*.md" -print | xargs sed -i -e 's/{{ site.posts_images }}/{{ site.baseurl }}\/{{ site.posts_images }}/g'
$ sips --resampleWidth 340 *.jpg *.png
