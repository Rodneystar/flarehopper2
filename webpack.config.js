var path = require('path');

module.exports = {
    entry: './src/main/js/app.tsx',
    devtool: 'sourcemaps',
    devServer: {
        port: 9000,
        host: "0.0.0.0",
        proxy: {
            '/mode': 'http://localhost:8080',
            '/timers':  'http://localhost:8080'
        },
        contentBase: path.join(__dirname, '/src/main/resources/static/built')
    },
    mode: 'development',
    output: {
        path: path.join(__dirname, 'src/main/resources/static/built/'),
        filename: 'bundle.js'
    },
    resolve: {
        extensions: [".ts", ".tsx", ".js", ".jsx"]
    },
    module: {
        rules: [
            {
                test: /\.(t|j)sx?$/,
                include: path.resolve(__dirname, "src/main/js"),
                use: [
                        'ts-loader'
                        // {
                        //     loader: 'babel-loader',
                        //     options: {
                        //        presets: ["@babel/preset-env", "@babel/preset-react"]
                        //     }
                        //  }
                    ]
                }, { 
                    enforce: "pre", 
                    test: /\.js$/, 
                    loader: "source-map-loader" 
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