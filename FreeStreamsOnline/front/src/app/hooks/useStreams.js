import { useEffect, useState } from "react";
import * as CategoryAPI from "../api/category";
import * as PlaylistAPI from "../api/playlist";
import * as API from "../api/stream";

export function useStreams() {
    const [streams, setStreams] = useState([]);
    const [categories, setCategories] = useState([]);
    const [playlists, setPlaylists] = useState([]);
    const [pages, setPages] = useState({
        current: 1,
        total: 1,
        size: 6,
        totalItems: 0,
        hasNext: false,
        hasPrevious: false,
    });

    const [filters, setFilters] = useState({
        categoryId: "",
        playlistId: "",
    });

    const [sorting, setSorting] = useState({
        sortBy: null,
        sortDirection: "asc",
    });

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Перезагружаем данные при изменении размера страницы
        load(1); // Всегда начинаем с первой страницы при изменении размера
    }, [pages.size]);

    useEffect(() => {
        load(pages.current);
    }, [pages.current, filters.categoryId, filters.playlistId, sorting.sortBy, sorting.sortDirection]);

    async function load(page = 1) {
        try {
            setLoading(true);

            const [streamsData, categoriesData, playlistsData] = await Promise.all([
                API.fetchStreams(
                    page,
                    pages.size,
                    filters.categoryId || null,
                    filters.playlistId || null,
                    sorting.sortBy,
                    sorting.sortDirection
                ),
                CategoryAPI.fetchCategories(),
                PlaylistAPI.fetchPlaylists(),
            ]);

            console.log("Sorted streams data:", streamsData);

            const streamsItems = streamsData.items || [];

            setPages((prev) => ({
                ...prev,
                current: streamsData.currentPage || page,
                total: streamsData.totalPages || 1,
                size: streamsData.currentSize || pages.size,
                totalItems: streamsData.totalItems || streamsItems.length,
                hasNext: streamsData.hasNext || false,
                hasPrevious: streamsData.hasPrevious || false,
            }));

            const extended = streamsItems.map((stream) => ({
                ...stream,
                id: String(stream.id),
                publicationDate: stream.publication_date || stream.publicationDate,
                views: stream.views || 0,
                playlistId: stream.playlist ? String(stream.playlist.id) : "",
                categories: stream.categories || [],
                category: stream.categories && stream.categories.length > 0 ? stream.categories[0] : null,
                categoryId: stream.categories && stream.categories.length > 0 ? String(stream.categories[0].id) : "",
                playlist: stream.playlist,
            }));

            setStreams(extended);

            const categoriesItems = categoriesData.items || categoriesData.content || categoriesData;
            const playlistsItems = playlistsData.items || playlistsData.content || playlistsData;

            setCategories(Array.isArray(categoriesItems) ? categoriesItems : []);
            setPlaylists(Array.isArray(playlistsItems) ? playlistsItems : []);
        } catch (error) {
            console.error("Error loading data:", error);
            setCategories([]);
            setPlaylists([]);
            setStreams([]);
        } finally {
            setLoading(false);
        }
    }

    function sortAsc() {
        setSorting({
            sortBy: "name",
            sortDirection: "asc",
        });
        // Сбрасываем на первую страницу при сортировке
        setPages((prev) => ({ ...prev, current: 1 }));
    }

    function sortDesc() {
        setSorting({
            sortBy: "name",
            sortDirection: "desc",
        });
        // Сбрасываем на первую страницу при сортировке
        setPages((prev) => ({ ...prev, current: 1 }));
    }

    function applyFilters(categoryId = "", playlistId = "") {
        setFilters({
            categoryId: String(categoryId),
            playlistId: String(playlistId),
        });
        // Сбрасываем сортировку при изменении фильтров (опционально)
        setSorting({
            sortBy: null,
            sortDirection: "asc",
        });
        setPages((prev) => ({
            ...prev,
            current: 1,
            total: 1,
            totalItems: 0,
        }));
    }

    function resetFilters() {
        setFilters({
            categoryId: "",
            playlistId: "",
        });
        // Сбрасываем сортировку (опционально)
        setSorting({
            sortBy: null,
            sortDirection: "asc",
        });
        setPages((prev) => ({
            ...prev,
            current: 1,
            total: 1,
            totalItems: 0,
        }));
    }

    function changePage(page) {
        if (page >= 1 && page <= pages.total) {
            setPages((prev) => ({ ...prev, current: page }));
        }
    }

    function changePageSize(size) {
        const newSize = parseInt(size);
        if (newSize > 0) {
            setPages((prev) => ({
                ...prev,
                size: newSize,
                current: 1, // Сбрасываем на первую страницу
            }));
        }
    }

    const filteredStreams = streams.filter((stream) => {
        // Проверяем по списку категорий
        const matchesCategory =
            !filters.categoryId ||
            (stream.categories && stream.categories.some((cat) => String(cat.id) === filters.categoryId));

        // Проверяем по плейлисту
        const matchesPlaylist =
            !filters.playlistId || (stream.playlist && String(stream.playlist.id) === filters.playlistId);

        return matchesCategory && matchesPlaylist;
    });

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
        currentSorting: sorting, // Добавляем информацию о сортировке
        loading,
        pages,
        changePage,
        changePageSize,
        reload: () => load(pages.current),
        setSorting,
        setPages,
    };
}
