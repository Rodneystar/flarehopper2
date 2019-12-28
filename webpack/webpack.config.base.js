var path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
    entry: './src/main/js/app.tsx',
    output: {
        path: path.join(__dirname, '..', 'src/main/resources/static/built/'),
        filename: 'bundle.js'
    },
    resolve: {
        extensions: [".ts", ".tsx", ".js", ".jsx"]
    },
    plugins: [
        new CleanWebpackPlugin()
    ],
    module: {
        rules: [
            {
                test: /\.(t|j)sx?$/,
                include: path.resolve(__dirname, "..", "src/main/js"),
                use: ['ts-loader']
                }, { 
                    enforce: "pre", 
                    test: /\.js$/, 
                    loader: "source-map-loader" 
                },{
                    test: /\.s[ac]ss$/,
                    include: path.resolve(__dirname, "..", "src/main/styles"),
                    use: [
                        'style-loader',
                        'css-loader',
                        'sass-loader'
                    ]
                }
        ]
    },
    // externals: {
    //     "react": "React",
    //     "react-dom": "ReactDOM"
    // }
};