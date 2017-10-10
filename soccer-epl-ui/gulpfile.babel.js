import gulp from 'gulp';
import plumber from 'gulp-plumber';
import notify from 'gulp-notify';
import sass from 'gulp-sass';
import watch from 'gulp-watch';
import livereload from 'gulp-livereload';



gulp.task('styles', function() {
  return watch('./src/sass/**/*.scss', function() {
    gulp.src(['./src/sass/App.scss', './src/sass/vendor-styles.scss' ])
    .pipe(plumber({ errorHandler: notify.onError('Error: <%= error.message %>') }))
    .pipe(sass())
    .pipe(notify({
      title: 'Gulp',
      subtitle: 'success',
      message: 'Admin Sass Task',
      sound: 'Pop',
    }))
    .pipe(livereload())
    .pipe(gulp.dest('./src/static/css/'));
  });
});


gulp.task('watch', () => {
  livereload.listen();
  gulp.watch('./src/sass/**/*.scss', ['styles']);
});

gulp.task('default', ['styles']);
