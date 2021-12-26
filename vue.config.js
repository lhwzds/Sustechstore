module.exports = {
    devServer: {
        proxy:{
          
              '/root':{
                // target:'http://database.sustechstore.com:23333', // http://database.sustechstore.com
                target:'http://localhost:23333', 
                changeOrigin:true,
                pathRewrite:{'^/root':''}
              },
              '/proxy':{
                // target:'http://database.sustechstore.com:23334',
                target:'http://localhost:23333', 
                changeOrigin:true,
                pathRewrite:{'^/proxy':''}
              },
              '/a':{
                target:'http://10.25.110.131:23333/register',
                changeOrigin:true,
                pathRewrite:{'^/a':''}
              },
              '/tes':{
                target:'http://10.25.110.131:23333/test',
                changeOrigin:true,
                pathRewrite:{'^/tes':''}
              },
              '/b':{
                target:'http://10.25.110.131:23333/user/basic',
                changeOrigin:true,
                pathRewrite:{'^/b':''}
              },
              '/icon':{
                target:'http://10.25.110.131:23333/user/icon',
                changeOrigin:true,
                pathRewrite:{'^/icon':''}
              },
              
              '/img':{
                target:'http://10.25.110.131:23333/user/image',
                changeOrigin:true,
                pathRewrite:{'^/img':''
              },
              '/uu':{
                target:'http://10.25.110.131:23333/user/update',
                changeOrigin:true,
                pathRewrite:{'^/uu':''}
              },
            }
        }
    }
}