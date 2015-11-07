/**
 * Used by https://github.com/cybertk/abao/ for verification testing of the RAML spec
 */
var hooks = require('hooks');

hooks.before('POST /api/book -> 400', function(test,done){
        test.request.body={author:'steve'};
        done();
});
hooks.before('GET /api/book/{isbn} -> 200', function(test,done){
    test.request.params={isbn:'111-2'};
    done();
});
hooks.before('PUT /api/book/{isbn} -> 200', function(test,done){
    test.request.params={isbn:'111-1'};
    done();
});
hooks.before('DELETE /api/book/{isbn} -> 200', function(test,done){
    test.request.params={isbn:'111-1'};
    done();
});


