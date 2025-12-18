import { useNavigate } from 'react-router-dom';
import { StreamsList } from '../components/StreamsList';
import { useStreams } from '../hooks/useStreams';
// @ts-ignore
import { Pagination } from '../components/Pagination';

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
        currentSorting,
        loading,
        pages,
        changePage,
        reload,
        setSorting,
        setPages,
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
                            onClick={() => {
                                resetFilters();
                                reload();
                            }}
                            disabled={loading}
                        >
                            Сбросить фильтры
                        </button>
                    </div>
                </div>
            </div>
            {(currentFilters.categoryId || currentFilters.playlistId) && (
                <div className="alert alert-info mb-3">
                    <strong>Применены фильтры:</strong>
                    {currentFilters.categoryId && (
                        <span className="ms-2">
                            Категория: {categories.find(c => String(c.id) === currentFilters.categoryId)?.name}
                        </span>
                    )}
                    {currentFilters.playlistId && (
                        <span className="ms-2">
                            Плейлист: {playlists.find(p => String(p.id) === currentFilters.playlistId)?.name}
                        </span>
                    )}
                </div>
            )}

            <div className="buttons d-flex align-items-center gap-3 mb-3">
                <h2 className="mb-0">Начать новую трансляцию</h2>
                <button
                    className="btn btn-success"
                    onClick={() => navigate('/form')}
                >
                    Добавить
                </button>
                
                <div className="btn-group" role="group">
                    <button
                        className={`btn ${currentSorting.sortBy === 'name' && currentSorting.sortDirection === 'asc' ? 'btn-primary' : 'btn-outline-primary'}`}
                        onClick={sortAsc}
                    >
                        Отсортировать по возрастанию
                        {currentSorting.sortBy === 'name' && currentSorting.sortDirection === 'asc' && (
                            <i className="bi bi-arrow-up ms-1"></i>
                        )}
                    </button>
                    
                    <button
                        className={`btn ${currentSorting.sortBy === 'name' && currentSorting.sortDirection === 'desc' ? 'btn-primary' : 'btn-outline-primary'}`}
                        onClick={sortDesc}
                    >
                        Отсортировать по убыванию
                        {currentSorting.sortBy === 'name' && currentSorting.sortDirection === 'desc' && (
                            <i className="bi bi-arrow-down ms-1"></i>
                        )}
                    </button>
                    
                    {(currentSorting.sortBy === 'name') && (
                        <button
                            className="btn btn-outline-secondary"
                            onClick={() => {
                                setSorting({ sortBy: null, sortDirection: 'asc' });
                                setPages(prev => ({ ...prev, current: 1 }));
                            }}
                            title="Сбросить сортировку"
                        >
                            <i className="bi bi-x-lg"></i>
                        </button>
                    )}
                </div>
            </div>

            {(currentSorting.sortBy === 'name') && (
                <div className="alert alert-info mb-3">
                    <strong>Сортировка:</strong>
                    <span className="ms-2">
                        По названию ({currentSorting.sortDirection === 'asc' ? 'возрастание' : 'убывание'})
                    </span>
                </div>
            )}

            <div id="streamsList" className="row mt-1">
                <h3>Мои трансляции {pages.totalItems > 0 && `(${pages.totalItems})`}</h3>
                
                {loading ? (
                    <div className="alert alert-info">Загрузка данных...</div>
                ) : streams.length === 0 ? (
                    <div className="alert alert-info">
                        Нет трансляций, соответствующих выбранным фильтрам
                    </div>
                ) : (
                    <>
                        <div className="mb-3">
                            <small className="text-muted">
                                Страница {pages.current} из {pages.total} 
                                (показано {streams.length} из {pages.totalItems} трансляций)
                            </small>
                        </div>
                        
                        <StreamsList
                            streams={streams}
                            onEdit={(s) => navigate(`/form/${s.id}`)}
                            onDelete={remove}
                        />
                        
                        {/* Компонент пагинации */}
                        {pages.total > 1 && (
                            <Pagination 
                                currentPage={pages.current}
                                totalPages={pages.total}
                                onPageChange={changePage}
                            />
                        )}
                    </>
                )}
            </div>
        </main>
    );
};