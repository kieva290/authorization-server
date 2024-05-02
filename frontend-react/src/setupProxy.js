const { createProxyMiddleware } = require('http-proxy-middleware');

const rewriteFn = (p, r) => {
    return p.replace('/api', '/')
};

module.exports = function(app) {

  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://backend-gateway-client:8083',
      changeOrigin: true,
      pathRewrite: rewriteFn
    })
  );

};