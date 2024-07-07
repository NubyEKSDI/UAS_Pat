const express = require('express');
const mysql = require("mysql2");
const bcrypt = require("bcryptjs");

const app = express();
app.use(express.json());

const db = mysql.createConnection({
	host: "localhost",
	user: "root",
	password: "",
	database: "peminjaman_uppk",
});

db.connect((err) => {
	if (err) throw err;
	console.log("Database connected!");
});

// Sign Up User endpoint
app.post("/signup", (req, res) => {
	const { username, password, name, nrp, jurusan } = req.body;
	const hashedPassword = bcrypt.hashSync(password, 8);

	db.query(
		"INSERT INTO users (username, password, name, nrp, jurusan, access_level) VALUES (?, ?, ?, ?, ?, ?)",
		[username, hashedPassword, name, nrp, jurusan, 0],
		(err, result) => {
			if (err) return res.status(500).send("Server error");
			res.status(201).send("User registered");
		}
	);
});

// Sign Up Admin endpoint
app.post("/signup_admin", (req, res) => {
	const { username, password } = req.body;
	const hashedPassword = bcrypt.hashSync(password, 8);

	db.query(
		"INSERT INTO users (username, password, access_level) VALUES (?, ?, ?)",
		[username, hashedPassword, 1],
		(err, result) => {
			if (err) return res.status(500).send("Server error");
			res.status(201).send("Admin registered");
		}
	);
});

// Login endpoint
app.post("/login", (req, res) => {
	const { username, password } = req.body;

	db.query(
		"SELECT * FROM users WHERE username = ?",
		[username],
		(err, results) => {
			if (err) return res.status(500).send("Server error");
			if (results.length === 0) return res.status(404).send("User not found");

			const user = results[0];
			const passwordIsValid = bcrypt.compareSync(password, user.password);

			if (!passwordIsValid) return res.status(401).send("Invalid password");

			res.status(200).send({
				id: user.id,
				username: user.username,
				access_level: user.access_level,
			});
		}
	);
});

// Add Equipment endpoint
app.post("/add-equipment", (req, res) => {
	const { name, quantity } = req.body;

	db.query(
		"INSERT INTO equipment (name, quantity) VALUES (?, ?)",
		[name, quantity],
		(err, result) => {
			if (err) return res.status(500).send("Server error");
			res.status(201).send("Equipment added");
		}
	);
});

// Get Equipment endpoint
app.get("/equipment", (req, res) => {
	db.query("SELECT * FROM equipment", (err, results) => {
		if (err) return res.status(500).send("Server error");
		res.status(200).json(results);
	});
});

app.get("/available_equipment", (req, res) => {
	db.query("SELECT * FROM equipment WHERE quantity > 0", (err, results) => {
		if (err) return res.status(500).send("Server error");
		res.status(200).json(results);
	});
});

// Update Equipment Quantity endpoint
app.post("/add-equipment-quantity", (req, res) => {
	const { equipment_id, quantity } = req.body;

	db.query(
		"UPDATE equipment SET quantity = quantity + ? WHERE id = ?",
		[quantity, equipment_id],
		(err, result) => {
			if (err) return res.status(500).send("Server error");
			res.status(200).send("Equipment quantity updated");
		}
	);
});

app.post("/reduce-equipment-quantity", (req, res) => {
	const { equipment_id, quantity } = req.body;

	db.query(
		"UPDATE equipment SET quantity = quantity - ? WHERE id = ?",
		[quantity, equipment_id],
		(err, result) => {
			if (err) return res.status(500).send("Server error");
			res.status(200).send("Equipment quantity updated");
		}
	);
});

// Borrow Equipment endpoint
app.post("/borrow", (req, res) => {
	const { user_id, equipment_id, quantity } = req.body;

	// Check if the equipment is available in the required quantity
	db.query(
		"SELECT * FROM equipment WHERE id = ? AND quantity >= ?",
		[equipment_id, quantity],
		(err, results) => {
			if (err) return res.status(500).send("Server error");
			if (results.length === 0)
				return res
					.status(404)
					.send("Equipment not available in sufficient quantity");

			// Proceed with borrowing if equipment is available
			db.query(
				"UPDATE equipment SET quantity = quantity - ? WHERE id = ?",
				[quantity, equipment_id],
				(err, result) => {
					if (err) return res.status(500).send("Server error");

					db.query(
						"INSERT INTO borrow_records (user_id, equipment_id, quantity) VALUES (?, ?, ?)",
						[user_id, equipment_id, quantity],
						(err, result) => {
							if (err) return res.status(500).send("Server error");
							res.status(201).send("Equipment borrowed");
						}
					);
				}
			);
		}
	);
});

// Return Equipment endpoint
app.post("/return", (req, res) => {
	const { user_id, equipment_id, quantity } = req.body;

	db.query(
		"UPDATE equipment SET quantity = quantity + ? WHERE id = ?",
		[quantity, equipment_id],
		(err, result) => {
			if (err) return res.status(500).send("Server error");

			db.query(
				"UPDATE borrow_records SET returned_at = NOW() WHERE user_id = ? AND equipment_id = ? AND returned_at IS NULL",
				[user_id, equipment_id],
				(err, result) => {
					if (err) return res.status(500).send("Server error");
					res.status(200).send("Equipment returned");
				}
			);
		}
	);
});

// Delete Equipment endpoint
app.delete("/delete-equipment/:equipmentId", (req, res) => {
	const { equipmentId } = req.params;

	db.query(
		"DELETE FROM equipment WHERE id = ?",
		[equipmentId],
		(err, result) => {
			if (err) return res.status(500).send("Server error");
			res.status(200).send("Equipment deleted");
		}
	);
});

// Get Borrow Records endpoint
app.get("/borrow_records", (req, res) => {
	db.query("SELECT * FROM borrow_records", (err, results) => {
		if (err) return res.status(500).send("Server error");
		res.status(200).json(results);
	});
});

app.get("/check_borrow_records", (req, res) => {
	db.query(
		"SELECT * FROM borrow_records WHERE user_id = ?",
		[req.query.user_id],
		(err, results) => {
			if (err) return res.status(500).send("Server error");
			res.status(200).json(results);
		}
	);
});

app.listen(3000, () => {
	console.log("Server is running on port 3000");
});
