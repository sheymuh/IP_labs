const BASE = "http://localhost:8080/api/1.0/category";
export const fetchCategories = () =>
    fetch(`${BASE}`)
        .then((r) => r.json())
        .then((data) => data.items || data.content || data);
export const fetchCategory = (id) => fetch(`${BASE}/${id}`).then((r) => r.json());
export const createCategory = (g) =>
    fetch(BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(g),
    }).then((r) => r.json());
export const updateCategory = (id, g) =>
    fetch(`${BASE}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(g),
    }).then((r) => r.json());
export const deleteCategory = (id) => fetch(`${BASE}/${id}`, { method: "DELETE" });
