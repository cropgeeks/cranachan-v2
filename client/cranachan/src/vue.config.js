const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const webpack = require('webpack');
​
module.exports = {
	publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
​
	configureWebpack: {
		plugins: [
		  new BundleAnalyzerPlugin(),
//		  new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /en/)
		]
	  }
};