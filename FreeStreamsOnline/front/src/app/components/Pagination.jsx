export const Pagination = ({ currentPage, totalPages, onPageChange }) => {
    if (totalPages <= 1) return null;
    
    const pageNumbers = [];
    const maxPagesToShow = 5;
    
    let startPage = Math.max(1, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);
    
    if (endPage - startPage + 1 < maxPagesToShow) {
        startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
        pageNumbers.push(i);
    }
    
    return (
        <nav aria-label="Навигация по страницам">
            <ul className="pagination justify-content-center">
                {/* Кнопка "Первая страница" */}
                <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                    <button
                        className="page-link"
                        onClick={() => onPageChange(1)}
                        disabled={currentPage === 1}
                        aria-label="Первая страница"
                    >
                        <i className="bi bi-chevron-double-left"></i>
                    </button>
                </li>
                
                {/* Кнопка "Предыдущая" */}
                <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                    <button
                        className="page-link"
                        onClick={() => onPageChange(currentPage - 1)}
                        disabled={currentPage === 1}
                        aria-label="Предыдущая страница"
                    >
                        <i className="bi bi-chevron-left"></i>
                    </button>
                </li>
                
                {/* Многоточие в начале */}
                {startPage > 1 && (
                    <li className="page-item disabled">
                        <span className="page-link">...</span>
                    </li>
                )}
                
                {/* Номера страниц */}
                {pageNumbers.map(number => (
                    <li
                        key={number}
                        className={`page-item ${currentPage === number ? 'active' : ''}`}
                    >
                        <button
                            className="page-link"
                            onClick={() => onPageChange(number)}
                            aria-label={`Страница ${number}`}
                            aria-current={currentPage === number ? 'page' : undefined}
                        >
                            {number}
                        </button>
                    </li>
                ))}
                
                {/* Многоточие в конце */}
                {endPage < totalPages && (
                    <li className="page-item disabled">
                        <span className="page-link">...</span>
                    </li>
                )}
                
                {/* Кнопка "Следующая" */}
                <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
                    <button
                        className="page-link"
                        onClick={() => onPageChange(currentPage + 1)}
                        disabled={currentPage === totalPages}
                        aria-label="Следующая страница"
                    >
                        <i className="bi bi-chevron-right"></i>
                    </button>
                </li>
                
                {/* Кнопка "Последняя страница" */}
                <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
                    <button
                        className="page-link"
                        onClick={() => onPageChange(totalPages)}
                        disabled={currentPage === totalPages}
                        aria-label="Последняя страница"
                    >
                        <i className="bi bi-chevron-double-right"></i>
                    </button>
                </li>
            </ul>
        </nav>
    );
};