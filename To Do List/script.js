let tasksData = {};

const todo = document.querySelector("#todo");
const progress = document.querySelector("#progress");
const done = document.querySelector("#done");

let dragElement = null;

// ---------- Reusable Functions -----------
function updateCount() {
    [todo, progress, done].forEach(col => {
        const tasks = col.querySelectorAll(".task");
        const count = col.querySelector(".right");
        count.innerText = tasks.length;
    });
}

function saveToLocalStorage() {
    [todo, progress, done].forEach(col => {
        const tasks = col.querySelectorAll(".task");

        tasksData[col.id] = Array.from(tasks).map(t => {
            return {
                title: t.querySelector("h2").innerText,
                desc: t.querySelector("p").innerText
            };
        });
    });

    localStorage.setItem("tasks", JSON.stringify(tasksData));
}

// ---------- Load from Local Storage ----------
if (localStorage.getItem("tasks")) {
    const data = JSON.parse(localStorage.getItem("tasks"));

    for (const col in data) {
        const column = document.querySelector(`#${col}`);

        data[col].forEach(task => {
            const div = document.createElement("div");

            div.classList.add("task");
            div.setAttribute("draggable", "true");

            div.innerHTML = `
                <h2>${task.title}</h2>
                <p>${task.desc}</p>
                <button>Delete</button>`;

            column.appendChild(div);

            div.addEventListener("dragstart", () => {
                dragElement = div;
            });
        });
    }

    updateCount();
}


// ---------- Drag Events for Columns ----------
function addDragEventsOnColumn(column) {
    column.addEventListener("dragenter", (e) => {
        e.preventDefault();
        column.classList.add("hover-over");
    });

    column.addEventListener("dragleave", (e) => {
        e.preventDefault();
        column.classList.remove("hover-over");
    });

    column.addEventListener("dragover", (e) => {
        e.preventDefault();
    });

    column.addEventListener("drop", (e) => {
        e.preventDefault();
        column.appendChild(dragElement);
        column.classList.remove("hover-over");

        updateCount();
        saveToLocalStorage();
    });
}

addDragEventsOnColumn(todo);
addDragEventsOnColumn(progress);
addDragEventsOnColumn(done);


// ---------- Add Task Popup ----------
const toggleModalButton = document.querySelector("#toggle-modal");
const modalBg = document.querySelector(".modal .bg");
const modal = document.querySelector(".modal");

toggleModalButton.addEventListener("click", () => {
    modal.classList.toggle("active");

        // Clear fields when modal opens
    document.querySelector("#task-title-input").value = "";
    document.querySelector("#task-desc-input").value = "";
});

modalBg.addEventListener("click", () => {
    modal.classList.remove("active");
});


// ---------- Add New task ----------
const addTaskButton = document.querySelector(".add-task-btn");

addTaskButton.addEventListener("click", () => {
    const taskTitle = document.querySelector("#task-title-input").value;
    const taskDesc = document.querySelector("#task-desc-input").value;

    const div = document.createElement("div");

    div.classList.add("task");
    div.setAttribute("draggable", "true");

    div.innerHTML = `
        <h2>${taskTitle}</h2>
        <p>${taskDesc}</p>
        <button>Delete</button>`;

    todo.appendChild(div);

    div.addEventListener("dragstart", () => {
        dragElement = div;
    });

    const deleteButton = div.querySelector("button");
    deleteButton.addEventListener("click", () => {
        div.remove();
        updateCount();

    })

    updateCount();
    saveToLocalStorage();


    modal.classList.remove("active");
});
