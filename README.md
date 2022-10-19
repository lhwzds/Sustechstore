# sustech-store

![](https://img.shields.io/github/stars/lhwzds/Sustechstore.svg)

  这是一个基于VUE和Springboot的课程项目

  This is a course project based on VUE and Springboot

  感谢[yechen17](https://github.com/yechen17) , [XJC-git](https://github.com/XJC-git) , [raven3199](https://github.com/raven3199) , [xiao xixi](https://github.com/tian-y-x) , [lhwzds](https://github.com/lhwzds)一起合作完成了这个课程项目。

  Thanks to [yechen17](https://github.com/yechen17) , [XJC-git](https://github.com/XJC-git) , [raven3199](https://github.com/raven3199) , [xiao xixi](https://github.com/tian-y-x) , [ lhwzds](https://github.com/lhwzds) collaborated together on this course project.

## Project summary

这是一个二手物品的交易网站项目。用户可以在上面将自己闲置的物品给挂在平台上售卖，同时还可以将自己需要的东西发布在平台上进行求购，还可以发布一些跑腿和代购的信息。我们对每个商品都标注了价格，分类，标签等基本信息。用户还可以通过搜索栏检索商品，以挑选自己喜欢的商品，如果用户想要和买家或卖家沟通，我们也搭建了实时聊天系统，用户和用户之前可以随时进行聊天，其中聊天记录也被记录保存在后端的数据库中。我们还集成了邮件通知功能，每当用户受到聊天信息或者用户的商品被购买了的时候，后端会向用户信息中登记的邮箱发送通知邮件，以便用户及时回复消息和发货。我们还集成了用户信用系统在网站中，如果用户太多次不遵守平台的规则，则会被禁止使用网站。我们整体的项目是采用Vue.js+elementui作为前端项目框架，Springboot+Postgresql作为后端框架和数据库，另外我们使用socket来构建了用户聊天系统并集成在了前后端中。

  This is a website for second-hand items trading. In this web development project, users can put their unused items on the platform for sale, or post request for needed items on the platform, as well as post some delivery needs on website. Each item is marked with basic information such as price, category and label. If users want to communicate with buyers or sellers, we have also built a real-time chat system where users and users can chat at any time before, where chat logs are also recorded and saved in the back-end database. We also integrated email notification function, whenever a user receives a chat message or a user's item is purchased, the back-end will send a notification email to the email address registered in the user's information, so that the user can reply to the message and ship the item in time. We also integrated a user credit system into the website, so that if a user does not comply with the platform's rules too many times, he or she will be banned from using the site. Our overall project is using Vue.js+elementui as front-end project framework, Springboot+Postgresql as back-end framework and database, in addition we used Socket to build the user chat system and integrated it in front and back-end. 


## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
