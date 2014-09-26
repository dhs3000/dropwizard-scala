
var require = {

    paths: {
        jquery: 'lib/jquery-2.1.1.min',
        lodash: 'lib/lodash.min'
    },

    map: {
      // '*' means all modules will get 'jquery-private'
      // for their 'jquery' dependency.
      '*': { 'jquery': 'lib/jquery-private' },

      // 'jquery-private' wants the real jQuery module
      // though. If this line was not here, there would
      // be an unresolvable cyclic dependency.
      'lib/jquery-private': { 'jquery': 'jquery' }
    }
};
