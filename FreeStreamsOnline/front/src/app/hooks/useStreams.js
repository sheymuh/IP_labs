import { useEffect, useState } from "react";
import * as CategoryAPI from "../api/category";
import * as PlaylistAPI from "../api/playlist";
import * as API from "../api/stream";

export function useStreams() {
    const [streams, setStreams] = useState([]);
    const [categories, setCategories] = useState([]);
    const [playlists, setPlaylists] = useState([]);

    const [filters, setFilters] = useState({
        categoryId: "",
        playlistId: "",
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        load();
    }, []);

    async function load() {
        try {
            setLoading(true);
            const [streamsData, categoriesData, playlistsData] = await Promise.all([
                API.fetchStreams(),
                CategoryAPI.fetchCategories(),
                PlaylistAPI.fetchPlaylists(),
            ]);

            console.log("Streams data:", streamsData);
            console.log("Categories data:", categoriesData);
            console.log("Playlists data:", playlistsData);

            // ИЗМЕНЕНИЕ: Извлекаем items из Page объекта
            const streamsItems = streamsData.items || streamsData.content || streamsData;
            const categoriesItems = categoriesData.items || categoriesData.content || categoriesData;
            const playlistsItems = playlistsData.items || playlistsData.content || playlistsData;

            const extended = streamsItems.map((stream) => ({
                ...stream,
                id: String(stream.id),
                playlistId: stream.playlist ? String(stream.playlist.id) : "",
                categoryId: stream.category ? String(stream.category.id) : "",
                // Если категории приходят как массив в stream.categories
                categories: stream.categories || [],
                category: stream.category,
                playlist: stream.playlist,
            }));

            setStreams(extended);
            setCategories(Array.isArray(categoriesItems) ? categoriesItems : []);
            setPlaylists(Array.isArray(playlistsItems) ? playlistsItems : []);
        } catch (error) {
            console.error("Error loading data:", error);
            setCategories([]);
            setPlaylists([]);
        } finally {
            setLoading(false);
        }
    }

    function applyFilters(categoryId = "", playlistId = "") {
        setFilters({
            categoryId: String(categoryId),
            playlistId: String(playlistId),
        });
    }

    function resetFilters() {
        setFilters({
            categoryId: "",
            playlistId: "",
        });
    }

    const filteredStreams = streams.filter((stream) => {
        const matchesCategory =
            !filters.categoryId ||
            (stream.categories && stream.categories.some((cat) => String(cat.id) === filters.categoryId)) ||
            String(stream.categoryId) === filters.categoryId;

        const matchesPlaylist = !filters.playlistId || String(stream.playlistId) === filters.playlistId;

        return matchesCategory && matchesPlaylist;
    });

    function sortAsc() {
        const sorted = [...streams].sort((a, b) => a.name.localeCompare(b.name));
        setStreams(sorted);
    }

    function sortDesc() {
        const sorted = [...streams].sort((a, b) => b.name.localeCompare(a.name));
        setStreams(sorted);
    }

    async function remove(id) {
        await API.deleteStream(String(id));
        await load();
    }

    async function save(stream) {
        const streamWithStringIds = {
            ...stream,
            playlistId: String(stream.playlistId),
            categoryIds: stream.categories ? stream.categories.map((cat) => String(cat.id)) : [],
        };

        if (stream.id) {
            await API.updateStream(String(stream.id), streamWithStringIds);
        } else {
            await API.createStream(streamWithStringIds);
        }
        await load();
    }

    return {
        streams: filteredStreams,
        allStreams: streams,
        categories,
        playlists,
        remove,
        save,
        sortAsc,
        sortDesc,
        applyFilters,
        resetFilters,
        currentFilters: filters,
        loading,
    };
}
