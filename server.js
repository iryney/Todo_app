var express = require('express');
var app = express();
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
var methodOverride = require('method-override');


mongoose.connect('mongodb://localhost/todo');
app.use(express.static(__dirname + '/public'));
app.use(bodyParser.urlencoded({'extended': 'true'}));
app.use(bodyParser.json());
app.use(bodyParser.json({type: 'application/vnd.api+json' }));
app.use(methodOverride());

app.listen(8080);
console.log("App listening on port 8080");

//schema for database
var Todo = mongoose.model('Todo', {
	text: String,
	date: Date,
	done: Boolean
});


//routing
app.get('#', function(req,res){
	res.sendfile('./public/index.html');
});

//get todos
app.get('/api/todos', function(req, res){
	Todo.find(function(err, todos){
		if(err)
			res.send(err)
		res.json(todos)
	});
});

//post new todo
app.post('/api/todos', function(req, res){
	Todo.create({
		text: req.body.text,
		date: Date.now(),
		done: false
	}, function(err, todo) {
		if (err)
			res.send(err);
		Todo.find(function(err, todos) {
			if (err)
				res.send(err)
			res.json(todos);
		});
	});
});

//delete todo from database
app.delete('/api/todos/:todo_id', function(req, res) {
        Todo.remove({
            _id : req.params.todo_id
        }, function(err, todo) {
            if (err)
                res.send(err);

            Todo.find(function(err, todos) {
                if (err)
                    res.send(err)
                res.json(todos);
            });
        });
});