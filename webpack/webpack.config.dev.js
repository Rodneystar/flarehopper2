var merge = require('webpack-merge');
var path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = merge( {
    devtool: 'sourcemaps',
    mode: 'development',
    plugins: [
        new HtmlWebpackPlugin({ template: './webpack/devServerTemplate.html' })
    ],
    devServer: {
        port: 9000,
        host: "0.0.0.0",
        proxy: {
            '/mode': 'http://localhost:8080',
            '/timers':  'http://localhost:8080'
        },
        contentBase: path.resolve(__dirname, '..','src/main/resources/static/built/')
    }
}, require('./webpack.config.base.js'))