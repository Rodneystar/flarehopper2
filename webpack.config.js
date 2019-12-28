module.exports = function( env ) {
    return require(`./webpack/webpack.config.${env}.js`)
}
