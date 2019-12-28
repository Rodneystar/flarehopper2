var merge = require('webpack-merge');

module.exports = merge( {
    mode: 'production',
}, require('./webpack.config.base.js'))