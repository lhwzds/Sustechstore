module.exports = {
    devServer: {
        proxy:{
              '/root':{
                // target:'http://10.15.223.52:23333', // http://database.sustechstore.com’
                target:'http://localhost:23333',
                changeOrigin:true,
                pathRewrite:{'^/root':''}
              },
              '/proxy':{
                target:'http://database.sustechstore.com:23334',
                changeOrigin:true,
                pathRewrite:{'^/proxy':''}
              },
        }
    }
}