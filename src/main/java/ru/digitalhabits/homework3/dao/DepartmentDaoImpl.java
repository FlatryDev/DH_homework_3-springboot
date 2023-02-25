package ru.digitalhabits.homework3.dao;

import org.springframework.stereotype.Repository;
import ru.digitalhabits.homework3.domain.Department;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("DepartmentDao")
public class DepartmentDaoImpl
        implements DepartmentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Nullable
    @Override
    public Department findById(@Nonnull Integer id) {
        return entityManager.find(Department.class, id);
    }

    @Nonnull
    @Override
    public List<Department> findAll() {
        return entityManager.createQuery("select d from Department d").getResultList();
    }

    @Nonnull
    @Override
    public Department update(@Nonnull Department department) {
        if (department.getId() == null) {
            entityManager.persist(department);
            return department;
        } else {
            return entityManager.merge(department);
        }
    }

    @Nullable
    @Override
    public Department delete(@Nonnull Integer id) {
        Department department = findById(id);
        if (department != null)
            entityManager.remove(department);
        return null;
    }
}
