let token = "";

function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
    })
        .then(res => res.json())
        .then(data => {
            token = data.token;
            document.getElementById("login-status").textContent = "Login successful!";

        })
        .catch(() => alert("Login failed"));
}

function addTask() {
    const name = document.getElementById("taskName").value;
    const date = document.getElementById("taskDate").value;

    fetch("http://localhost:8080/task/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ taskName: name, taskDate: date })
    })
        .then(res => {
            if (res.ok) alert("Task added");
            else alert("Error adding task");
        });
}

function getTasks() {
    fetch("http://localhost:8080/task/get", {
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => {
            const container = document.getElementById("tasks");
            container.innerHTML = "";
            data.forEach(task => {
                const div = document.createElement("div");
                div.textContent = `${task.taskName} - ${task.date}`;
                container.appendChild(div);
            });
        });
}

function searchTask() {
    const name = document.getElementById("searchName").value;

    fetch(`http://localhost:8080/task/get/name/${name}`, {
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(task => {
            const div = document.getElementById("searchResult");
            div.innerHTML = task ? `${task.taskName} - ${task.taskDate}` : "Not found";
        });
}

function deleteTask() {
    const id = document.getElementById("deleteId").value;

    fetch(`http://localhost:8080/task/delete/${id}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => {
            if (res.ok) alert("Deleted successfully");
            else alert("Delete failed");
        });
}