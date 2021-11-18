import  webpack from "webpack";
import  HtmlWebPackPlugin from "html-webpack-plugin";
import  MiniCssExtractPlugin from "mini-css-extract-plugin";
import { CheckerPlugin } from 'awesome-typescript-loader'
 
import  path from "path";

const isDevelopment = false;

const htmlPlugin = new HtmlWebPackPlugin({
  template: "./public/index.html"
});

const cssPlugin = new MiniCssExtractPlugin({
  filename: isDevelopment ? '[name].css' : '[name].[hash].css',
  chunkFilename: isDevelopment ? '[id].css' : '[id].[hash].css'
});

const config: webpack.Configuration = {
  mode: "development",
  entry: "./src/index.tsx",

  output: {
    publicPath: '/'
  },

  resolve: {
    extensions: ['.ts', '.tsx', '.js'],
    modules: [ path.resolve(__dirname, 'src'), 'node_modules' ]
  },

  module: {
    rules: [
      // All files with a '.ts' or '.tsx' extension will be handled by 'awesome-typescript-loader'.
      { 
        test: /\.tsx?$/, 
        loader: "awesome-typescript-loader" 
      },
      {
        test: /\.css$/i,
        use: [
          { loader: 'style-loader', options: { injectType: 'linkTag' } },
          { loader: 'file-loader' }
        ]
      },
      {
        test: /\.module\.s(a|c)ss$/,
        use: [
          { loader: MiniCssExtractPlugin.loader },
          {
            loader: 'css-loader',
            options: {
              modules: true,
              sourceMap: isDevelopment
            }
          },
          {
            loader: 'sass-loader',
            options: {
              sourceMap: isDevelopment
            }
          }
        ]
      },
      {
        test: /\.s(a|c)ss$/,
        exclude: /\.module.(s(a|c)ss)$/,
        use: [
          { loader: MiniCssExtractPlugin.loader },
          { loader: 'css-loader' },
          {
            loader: 'sass-loader',
            options: { sourceMap: isDevelopment }
          }
        ]
      }
    ],
  },
  devServer: {
    hot: true,
    port: 3000,
    historyApiFallback: true,
    //compress: true,
    //open: 'Google Chrome',
    static: [ path.join(__dirname, 'public') ],
    /*
    headers: {
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept",
      "Access-Control-Allow-Origin": "http://localhost:7080"
    }
    */
  },
  plugins: [cssPlugin, htmlPlugin, new CheckerPlugin()]
};

export default config;
