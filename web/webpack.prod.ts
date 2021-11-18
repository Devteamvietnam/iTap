import  webpack from "webpack";
import  HtmlWebPackPlugin from "html-webpack-plugin";
import  MiniCssExtractPlugin from "mini-css-extract-plugin";
import  TerserPlugin from "terser-webpack-plugin";
import  path from "path";

const isDevelopment = false;

const htmlPlugin = new HtmlWebPackPlugin({
  template: "./public/index.html",
  filename: "./index.html"
});

const cssPlugin = new MiniCssExtractPlugin({
  filename: isDevelopment ? '[name].css' : '[name].[hash].css',
  chunkFilename: isDevelopment ? '[id].css' : '[id].[hash].css'
});

const config: webpack.Configuration = {
  mode: "production",
  entry: "./src/index.tsx",

  output: {
    path: path.resolve(__dirname, "dist"),
    publicPath: '/',
    filename: "js/bundle.js"
  },

  optimization: {
    minimize: true,
    minimizer: [
      new TerserPlugin({ extractComments: false })
    ]
  },

  resolve: {
    extensions: ['.ts', '.tsx', '.js'],
    modules: [path.resolve(__dirname, 'src'), 'node_modules']
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
          {loader: 'style-loader', options: { injectType: 'linkTag' }},
          {loader: 'file-loader'}
        ]
      },
      {
        test: /\.module\.s(a|c)ss$/,
        use: [
          { loader: MiniCssExtractPlugin.loader },
          {
            loader: 'css-loader',
            options: { modules: true, sourceMap: isDevelopment }
          },
          {
            loader: 'sass-loader',
            options: { sourceMap: isDevelopment }
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
    ]
  },
  plugins: [cssPlugin, htmlPlugin]
};

export default config;
