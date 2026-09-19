// webpack.config.js
module.exports = {
    // ... 其他配置 ...
    devServer: {
        // ... 其他devServer配置 ...
        allowedHosts: [
            'localhost', // 允许localhost访问
            '127.0.0.1', // 允许IPv4本地地址访问
            // 如需添加其他主机，直接添加非空字符串
        ],
        // ... 其他devServer配置 ...
    },
    // ... 其他配置 ...
};