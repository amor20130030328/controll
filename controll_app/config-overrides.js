const { override, addWebpackAlias } = require("customize-cra");
const path = require("path");

module.exports = override(
    // 路径别名配置
    addWebpackAlias({
        '@': path.resolve('./src')
    }),
    // 添加devServer配置（覆盖默认配置）
    (config) => {
        config.devServer = config.devServer || {};
        // 正确配置：要么用数组（不含'all'），要么用单独的'all'字符串
        config.devServer.allowedHosts = [
            'localhost',
            '127.0.0.1'
        ];
        // 如需允许所有主机，可改为：
        // config.devServer.allowedHosts = 'all';
        return config;
    }
);