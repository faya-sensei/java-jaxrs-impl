import { AUTH_AUTHORIZED, AUTH_LOADING } from "./components/auth-event.js";
import { AuthPanel } from "./components/auth-panel.js";
import { PROJECT_LOADING, PROJECT_FORWARD } from "./components/project-event.js";
import { ProjectPanel } from "./components/project-panel.js";
import { TaskComponent } from "./components/task-component.js";
import { TASK_LOADING } from "./components/task-event.js";
import { TaskStatus } from "./components/task-status.js";
import { TaskPanel } from "./components/task-panel.js";
import { verify } from "./api.js";

const app = document.getElementById("app");

const panels = {
    auth: new AuthPanel(),
    project: new ProjectPanel(),
    task: new TaskPanel()
};

panels.auth.addEventListener(AUTH_AUTHORIZED, ({ detail }) => {
    panels.project.dispatchEvent(new CustomEvent(PROJECT_LOADING, { detail }));
});

panels.project.addEventListener(PROJECT_FORWARD, ({ detail }) => {
    panels.task.dispatchEvent(new CustomEvent(TASK_LOADING, { detail }));
})

app.append(...Object.values(panels));

verify().then(result => {
    if (result) {
        panels.project.dispatchEvent(new CustomEvent(PROJECT_LOADING, {
            detail: {
                id: result.id
            }
        }));
    } else {
        panels.auth.dispatchEvent(new Event(AUTH_LOADING));
    }
});
