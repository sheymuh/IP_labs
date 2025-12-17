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

            const extended = streamsData.map((stream) => ({
                ...stream,
                id: String(stream.id),
                playlistId: stream.playlist ? String(stream.playlist.id) : "",
                categoryId: stream.category ? String(stream.category.id) : "",
                category: stream.category,
                playlist: stream.playlist,
            }));

            setStreams(extended);
            setCategories(categoriesData || []);
            setPlaylists(playlistsData || []);
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
        const matchesCategory = !filters.categoryId || String(stream.categoryId) === filters.categoryId;
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
        const saveStream = {
            name: stream.name,
            image: stream.image,
            description: stream.description,
            playlistId: Number(stream.playlistId),
            categoryId: Number(stream.categoryId),
        };

        if (stream.id) {
            await API.updateStream(String(stream.id), saveStream);
        } else {
            await API.createStream(saveStream);
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
