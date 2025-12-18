const BASE = "http://localhost:8080/api/1.0/stream";

export const fetchStreams = () =>
    fetch(`${BASE}`)
        .then((r) => r.json())
        .then((data) => data.items || data.content || data); // Извлекаем items

export const fetchStream = (id) => fetch(`${BASE}/${id}`).then((r) => r.json());

export const createStream = (s) =>
    fetch(BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(s),
    }).then((r) => r.json());

export const updateStream = (id, s) =>
    fetch(`${BASE}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(s),
    }).then((r) => r.json());

export const deleteStream = (id) =>
    fetch(`${BASE}/${id}`, {
        method: "DELETE",
    }).then((r) => r.json());
