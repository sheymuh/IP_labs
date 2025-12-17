import { useNavigate } from 'react-router-dom';
import { StreamsList } from '../components/StreamsList';
import { useStreams } from '../hooks/useStreams';

export const AccountPage = () => {
    const { 
        streams, 
        categories, 
        playlists, 
        remove, 
        sortAsc,
        sortDesc,
        applyFilters,
        resetFilters,
        currentFilters,
        loading,
    } = useStreams();
    
    const navigate = useNavigate();

    const handleCategoryFilter = (categoryId) => {
        applyFilters(categoryId, currentFilters.playlistId);
    };

    const handlePlaylistFilter = (playlistId) => {
        applyFilters(currentFilters.categoryId, playlistId);
    };

    return (
        <main className="flex-grow-1 pt-2">
            <h2><em>Ваш никнейм</em> <i className="bi bi-patch-check-fill"></i></h2>
            <div className="avatar d-flex"><img src="/derzko.webp" alt="derzko" /></div>
            <p>ПОЛ МИЛЛИОНА ПОДПИЩИКОВ</p>
            
            {/* Секция фильтров */}
            <div className="filters-section mb-4 p-3 border rounded">
                <h4>Фильтры</h4>

                <div className="row">
                    <div className="col-md-4">
                        <label className="form-label">Фильтр по категории:</label>
                        <select 
                            className="form-select"
                            value={currentFilters.categoryId}
                            onChange={(e) => handleCategoryFilter(e.target.value)}
                            disabled={loading}
                        >
                            <option value="">Все категории</option>
                            {categories.map(category => (
                                <option key={category.id} value={String(category.id)}>
                                    {category.name || `Категория ${category.id}`}
                                </option>
                            ))}
                        </select>
                        {categories.length === 0 && !loading && (
                            <div className="text-danger small">Нет доступных категорий</div>
                        )}
                    </div>
                    <div className="col-md-4">
                        <label className="form-label">Фильтр по плейлисту:</label>
                        <select 
                            className="form-select"
                            value={currentFilters.playlistId}
                            onChange={(e) => handlePlaylistFilter(e.target.value)}
                            disabled={loading}
                        >
                            <option value="">Все плейлисты</option>
                            {playlists.map(playlist => (
                                <option key={playlist.id} value={String(playlist.id)}>
                                    {playlist.name || `Плейлист ${playlist.id}`}
                                </option>
                            ))}
                        </select>
                        {playlists.length === 0 && !loading && (
                            <div className="text-danger small">Нет доступных плейлистов</div>
                        )}
                    </div>
                    <div className="col-md-4 d-flex align-items-end">
                        <button 
                            className="btn btn-outline-secondary w-100"
                            onClick={resetFilters}
                            disabled={loading}
                        >
                            Сбросить фильтры
                        </button>
                    </div>
                </div>
            </div>

            <div className="buttons d-flex align-items-center gap-3 mb-3">
                <h2 className="mb-0">Начать новую трансляцию</h2>
                <button
                    className="btn btn-success"
                    onClick={() => navigate('/form')}
                >
                    Добавить
                </button>
                <button
                    className="btn btn-primary"
                    onClick={sortAsc}
                >
                    Отсортировать по возрастанию
                </button>
                
                <button
                    className="btn btn-primary"
                    onClick={sortDesc}
                >
                    Отсортировать по убыванию
                </button>
            </div>

            <div id="streamsList" className="row mt-1">
                <h3>Мои трансляции {streams.length > 0 && `(${streams.length})`}</h3>
                
                {loading ? (
                    <div className="alert alert-info">Загрузка данных...</div>
                ) : streams.length === 0 ? (
                    <div className="alert alert-info">
                        Нет трансляций, соответствующих выбранным фильтрам
                    </div>
                ) : (
                    <StreamsList
                        streams={streams}
                        onEdit={(s) => navigate(`/form/${s.id}`)}
                        onDelete={remove}
                    />
                )}
            </div>
        </main>
    );
};