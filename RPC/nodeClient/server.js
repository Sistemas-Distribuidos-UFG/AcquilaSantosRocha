const express = require('express');
const questions = require("./question.json");

const app = express();

app.set('view engine', 'pug');

app.use(express.static(__dirname + "/public"));


app.get('/', (req, res) => {
    res.render('index', {
        title: 'NodeJS Client',
        local_pub: __dirname + '/pug.gif',
        originalUrl: req.originalUrl
    });

});

app.get('/question', (req, res) => {
    const question = questions.numbers.find(q => q.id === req.query.id);
    res.render('question', {
        title: 'NodeJS Client',
        question,
    });
});

// app.engine('pug', require('pug').__express)



const server = app.listen(7000, () => {
    console.log(`Express running → PORT ${server.address().port}`);
});

