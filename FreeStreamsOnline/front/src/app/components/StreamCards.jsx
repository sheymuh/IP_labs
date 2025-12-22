export const StreamCards = ({ stream, onEdit, onDelete }) => {
    console.log('Stream in card:', {
        id: stream.id,
        name: stream.name,
        publicationDate: stream.publicationDate,
        publication_date: stream.publication_date,
        pubDate: stream.pubDate,
        allFields: Object.keys(stream)
    });
    
    const getPublicationDate = () => {
        return stream.publicationDate || 
               stream.publication_date || 
               stream.pubDate || 
               stream.publicationDate;
    };
    
    const getViews = () => {
        return stream.views || 0;
    };
    
    const formatViews = (views) => {
        return views.toLocaleString('ru-RU');
    };
    
    const formatDate = (dateString) => {
        if (!dateString) return "Не указана";
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('ru-RU', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            });
        } catch (e) {
            console.error('Error formatting date:', dateString, e);
            return dateString;
        }
    };
    
    const publicationDate = getPublicationDate();
    const views = getViews();

    return (
        <div className="card stream-card h-100">
            <div className="position-relative">
                <div className="d-flex justify-content-center align-items-center p-3">
                    <img 
                        src={stream.image} 
                        className="card-img-top" 
                        alt={stream.name} 
                        style={{ 
                            height: '200px',
                            width: '100%',
                            objectFit: 'cover',
                            borderRadius: '8px'
                        }}
                    />
                </div>
                
                <span className="position-absolute top-0 end-0 m-2 badge bg-dark bg-opacity-75">
                    <i className="bi bi-eye me-1"></i> {formatViews(views)}
                </span>
            </div>
            
            <div className="card-body d-flex flex-column">
                <h5 className="card-title">{stream.name}</h5>
                <p className="card-text text-muted mb-3">
                    <small>{stream.description}</small>
                </p>
                
                <div className="mt-auto">
                    {/* Информация о категориях */}
                    <div className="mb-3">
                        <div className="d-flex align-items-center mb-1">
                            <i className="bi bi-tag text-primary me-2"></i>
                            <small className="text-muted">Категории:</small>
                        </div>
                        <div className="categories-badges">
                            {stream.categories && stream.categories.length > 0 ? (
                                stream.categories.map(cat => (
                                    <span key={cat.id} className="badge bg-light text-dark me-1 mb-1 border">
                                        {cat.name}
                                        {cat.ageLimit > 0 && (
                                            <span className="ms-1 text-warning">({cat.ageLimit}+)</span>
                                        )}
                                    </span>
                                ))
                            ) : (
                                <span className="badge bg-secondary">Без категории</span>
                            )}
                        </div>
                    </div>
                    
                    {/* Информация о плейлисте */}
                    <div className="mb-3">
                        <div className="d-flex align-items-center mb-1">
                            <i className="bi bi-music-note-list text-success me-2"></i>
                            <small className="text-muted">Плейлист:</small>
                        </div>
                        <div>
                            <span className="badge bg-info text-dark">
                                {stream.playlist?.name || "Не указан"}
                            </span>
                        </div>
                    </div>
                    
                    {/* Информация о дате и просмотрах */}
                    <div className="d-flex justify-content-between align-items-center border-top pt-3">
                        <div>
                            <div className="d-flex align-items-center">
                                <i className="bi bi-calendar-event text-secondary me-2"></i>
                                <small className="text-muted">
                                    {formatDate(publicationDate)}
                                </small>
                            </div>
                        </div>
                        <div className="text-end">
                            <small className="text-muted d-block">
                                <i className="bi bi-eye me-1"></i>
                                {formatViews(views)} просмотров
                            </small>
                        </div>
                    </div>
                    
                    {/* Кнопки действий */}
                    <div className="card-buttons d-flex gap-2 mt-3">
                        <button 
                            className="btn btn-warning btn-sm flex-grow-1"
                            onClick={onEdit}
                            title="Редактировать трансляцию"
                        >
                            <i className="bi bi-pencil me-1"></i> Редактировать
                        </button>
                        <button 
                            className="btn btn-danger btn-sm"
                            onClick={onDelete}
                            title="Удалить трансляцию"
                        >
                            <i className="bi bi-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};