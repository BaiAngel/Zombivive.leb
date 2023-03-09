const router = require('express').Router();
const passport = require('passport');
const Customer = require('../models/user');
router.get('/', (req, res, next) => {
  res.render('index');
});

router.get('/singup', (req, res, next) => {
  res.render('singup');
});

router.post('/singup', passport.authenticate('local-signup', {
  successRedirect: '/profile.html',
  failureRedirect: '/singin.html',
  failureFlash: true
})); 

router.get('/singin', (req, res, next) => {
  res.render('singin');
});


router.post('/singin', passport.authenticate('local-signin', {
  successRedirect: '/profile.html',
  failureRedirect: '/singup.html',
  failureFlash: true
}));

router.get('/profile',isAuthenticated, (req, res, next) => {
  res.render('profile');
});

const bodyParser = require('body-parser');
router.use(bodyParser.json());

router.post('/score', (req, res) => {
  const username = req.body.username;
  const score = req.body.score;

  // Aquí puedes hacer lo que quieras con los datos, como almacenarlos en una base de datos o procesarlos de alguna otra manera
  console.log(`Usuario: ${username}, Puntaje: ${score}`);

  const newCustomer = new Customer({
    name: username,
    kills: score
  });
  newCustomer.save()
    .then(() => console.log('Customer saved'))
    .catch(err => console.log('Error saving customer:', err));

  res.send('¡Datos recibidos!');
  const Customers = Customer.find({});
  
  // Copiar cada usuario en una nueva colección
  for (let Customer of Customers) {
    const newUser = new User({
      name: Customer.name,
      kills: Customer.kills
      // Agregar cualquier otro campo que desees copiar
    }); newCustomer.save();
  }
  
  console.log('Copiado exitosamente!');
});


router.get('/logout', (req, res, next) => {
  req.logout();
  res.redirect('/');
});


function isAuthenticated(req, res, next) {
  if(req.isAuthenticated()) {
    return next();
  }

  res.redirect('/')
}

module.exports = router;