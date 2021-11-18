const gulp = require('gulp');
const replace = require('gulp-replace');
const rename = require('gulp-rename');
const merge = require("merge-stream");
const exec = require('child_process').exec;
const fs = require('fs');
const del = require('del');

var srcFiles = ['./public/**', './dist/**']
//var dest = "../../app/release/src/app/server/public"
var dest = "../../../releases/datatp/server/public"

gulp.task('react:do-clean', function() {
  return del('dist', {force:true});
});

gulp.task('react:do-build', function(callback) {
  exec('npm run build', function (err, stdout, stderr) {
    console.log(stdout);
    console.log(stderr);
    callback(err);
  });
});

gulp.task('react:clean', gulp.series('react:do-clean'));
gulp.task('react:build', gulp.series('react:do-clean', 'react:do-build'));

gulp.task('app:deploy-copy', function() {
  del(`${dest}/*`, {force:true})
  return merge([
    gulp.src(srcFiles).pipe(gulp.dest(dest))
  ]);
});

gulp.task('app:deploy', gulp.series('react:build',  'app:deploy-copy'));

gulp.task('clean', gulp.series('react:do-clean'));
gulp.task('help', function(cb) {
  console.log("help        Print this instructions");
  console.log(`app:deploy Deploy the react build to ${dest}`);
  console.log('\n')
  cb();
});

