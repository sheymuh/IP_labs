export const validatePublicationDate = (dateString) => {
    if (!dateString) {
        return { isValid: false, error: "Дата не может быть пустой" };
    }

    const regex = /^\d{4}-\d{2}-\d{2}$/;
    if (!regex.test(dateString)) {
        return { isValid: false, error: "Используйте формат YYYY-MM-DD" };
    }

    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
        return { isValid: false, error: "Некорректная дата" };
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (date > today) {
        return { isValid: false, error: "Дата не может быть в будущем" };
    }

    if (date.getFullYear() < 2020) {
        return { isValid: false, error: "Дата не может быть раньше 2020 года" };
    }

    return { isValid: true, error: "" };
};

export const getCurrentDate = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
};
