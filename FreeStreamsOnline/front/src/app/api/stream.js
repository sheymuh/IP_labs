const BASE = "http://localhost:8080/api/1.0/stream";

export const fetchStreams = (
    page = 1,
    size = 6,
    categoryId = null,
    playlistId = null,
    sortBy = null,
    sortDirection = "asc"
) => {
    let url = `${BASE}?page=${page}&size=${size}`;

    // Добавляем параметры фильтрации
    if (categoryId) {
        url += `&categoryId=${categoryId}`;
    }
    if (playlistId) {
        url += `&playlistId=${playlistId}`;
    }

    // Добавляем параметры сортировки
    if (sortBy) {
        url += `&sortBy=${sortBy}&sortDirection=${sortDirection}`;
    }

    return fetch(url)
        .then((r) => r.json())
        .then((data) => {
            console.log("Streams with sorting:", data);
            return data;
        });
};

export const fetchStream = (id) =>
    fetch(`${BASE}/${id}`).then((r) => {
        if (!r.ok) {
            throw new Error(`HTTP error! status: ${r.status}`);
        }
        return r.json();
    });

export const createStream = (streamData) => {
    console.log("Creating stream with FULL data:", JSON.stringify(streamData, null, 2));

    return fetch(BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(streamData),
    }).then((r) => {
        console.log("Create stream response status:", r.status);
        if (!r.ok) {
            return r.text().then((text) => {
                console.error("Server error response:", text);
                throw new Error(`HTTP ${r.status}: ${text}`);
            });
        }
        return r.json();
    });
};

export const updateStream = (id, streamData) => {
    console.log("Updating stream", id, "with FULL data:", JSON.stringify(streamData, null, 2));

    return fetch(`${BASE}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(streamData),
    }).then((r) => {
        console.log("Update stream response status:", r.status);
        if (!r.ok) {
            return r.text().then((text) => {
                console.error("Server error response:", text);
                throw new Error(`HTTP ${r.status}: ${text}`);
            });
        }
        return r.json();
    });
};

export const deleteStream = (id) =>
    fetch(`${BASE}/${id}`, {
        method: "DELETE",
    }).then((r) => r.json());
