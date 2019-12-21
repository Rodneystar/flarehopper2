var path = require('path');

module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    devServer: {
        port: 9000,

//        publicPath: '/src/main/resources/static/built/',
        contentBase: path.join(__dirname, '/src/main/resources/static/built')
    },
    mode: 'development',
    output: {
        path: path.join(__dirname, 'src/main/resources/static/built/'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                include: path.resolve(__dirname, "src/main/js"),
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },{
                test: /\.s[ac]ss$/,
                include: path.resolve(__dirname, "src/main/styles"),
                use: [
                  'style-loader',
                  'css-loader',
                  'sass-loader'
                ]
              }
        ]
    }
};