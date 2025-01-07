package org.example.providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import org.example.constants.ClientConsts;
import org.example.models.Client;
import org.example.models.NonStudent;
import org.example.models.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class ClientQueryProvider {
    private final CqlSession session;
    private final EntityHelper<Student> studentHelper;
    private final EntityHelper<NonStudent> nonStudentHelper;

    public ClientQueryProvider(MapperContext context, EntityHelper<Student> studentHelper, EntityHelper<NonStudent> nonStudentHelper) {
        this.session = context.getSession();
        this.studentHelper = studentHelper;
        this.nonStudentHelper = nonStudentHelper;
    }

    public void create(Client client) {
        session.execute(
                switch (client.getDiscriminator()) {
                    case "NonStudent" -> {
                        NonStudent nonStudent = (NonStudent) client;
                        yield session.prepare(nonStudentHelper.insert().build()).bind()
                                .setUuid(ClientConsts.ID, nonStudent.getId())
                                .setString(ClientConsts.DISCRIMINATOR, nonStudent.getDiscriminator())
                                .setFloat(ClientConsts.ADDITIONAL_FEE, nonStudent.getAdditionalFee())
                                .setString(ClientConsts.FIRST_NAME, nonStudent.getFirstName())
                                .setString(ClientConsts.LAST_NAME, nonStudent.getLastName())
                                .setInt(ClientConsts.MAX_BOOKS, nonStudent.getMaxBooks())
                                .setInt(ClientConsts.MAX_RENT_DAYS, nonStudent.getMaxRentDays());
                    }
                    case "Student" -> {
                        Student student = (Student) client;
                        yield session.prepare(studentHelper.insert().build()).bind()
                                .setUuid("id", student.getId())
                                .setString(ClientConsts.FIRST_NAME, student.getFirstName())
                                .setString(ClientConsts.LAST_NAME, student.getLastName())
                                .setString(ClientConsts.DISCRIMINATOR, student.getDiscriminator())
                                .setInt(ClientConsts.MAX_BOOKS, student.getMaxBooks())
                                .setInt(ClientConsts.MAX_RENT_DAYS, student.getMaxRentDays());
                    }
                    default -> throw new IllegalArgumentException("Unknown discriminator type: " + client.getDiscriminator());
                }
        );
    }

    public void delete(Client client) {
        session.execute(
                switch (client.getDiscriminator()) {
                    case "NonStudent" -> {
                        NonStudent nonStudent = (NonStudent) client;
                        yield session.prepare(nonStudentHelper.deleteByPrimaryKey().build()).bind()
                                .setUuid(ClientConsts.ID, nonStudent.getId())
                                .setString("discriminator", nonStudent.getDiscriminator());
                    }
                    case "Student" -> {
                        Student student = (Student) client;
                        yield session.prepare(studentHelper.deleteByPrimaryKey().build()).bind()
                                .setUuid(ClientConsts.ID, student.getId())
                                .setString("discriminator", student.getDiscriminator());
                    }
                    default -> throw new IllegalArgumentException("Unknown discriminator type: " + client.getDiscriminator());
                });
    }

    public Client findById(UUID id) {
        Select selectClient = QueryBuilder
                .selectFrom(ClientConsts.CLIENTS)
                .all()
                .where(Relation.column(ClientConsts.ID).isEqualTo(literal(id)));
        Row row = session.execute(selectClient.build()).one();
        String discriminator = row.getString(ClientConsts.DISCRIMINATOR);
        return switch (discriminator) {
            case "NonStudent" -> getNonStudent(row);
            case "Student" -> getStudent(row);
            default -> throw new IllegalArgumentException("Unknown discriminator type: " + discriminator);
        };
    }

    public List<Client> getAllClients() {
        Select all = QueryBuilder.selectFrom(ClientConsts.CLIENTS).all();
        List<Row> rows = session.execute(all.build()).all();
        List<Client> clients = new ArrayList<>();
        for (Row row : rows) {
            String discriminator = row.getString(ClientConsts.DISCRIMINATOR);
            if (discriminator.equals("NonStudent")) {
                clients.add(this.getNonStudent(row));
            } else {
                clients.add(this.getStudent(row));
            }
        }
        return clients;
    }

    public List<Client> getAllNonStudents() {
        Select all = QueryBuilder
                .selectFrom(ClientConsts.CLIENTS)
                .all()
                .allowFiltering()
                .where(Relation.column(ClientConsts.DISCRIMINATOR).isEqualTo(literal("NonStudent")));
        List<Row> rows = session.execute(all.build()).all();
        List<Client> clients = new ArrayList<>();
        for (Row row : rows) {
            clients.add(this.getNonStudent(row));
        }
        return clients;
    }

    public List<Client> getAllStudents() {
        Select all = QueryBuilder
                .selectFrom(ClientConsts.CLIENTS)
                .all()
                .allowFiltering()
                .where(Relation.column(ClientConsts.DISCRIMINATOR).isEqualTo(literal("Student")));
        List<Row> rows = session.execute(all.build()).all();
        List<Client> clients = new ArrayList<>();
        for (Row row : rows) {
            clients.add(this.getStudent(row));
        }
        return clients;
    }

    public void update(Client client) {
        session.execute(
                switch (client.getDiscriminator()) {
                    case "NonStudent" -> {
                        NonStudent nonStudent = (NonStudent) client;
                        yield session.prepare(nonStudentHelper.updateByPrimaryKey().build()).bind()
                                .setUuid(ClientConsts.ID, nonStudent.getId())
                                .setString(ClientConsts.DISCRIMINATOR, nonStudent.getDiscriminator())
                                .setFloat(ClientConsts.ADDITIONAL_FEE, nonStudent.getAdditionalFee())
                                .setString(ClientConsts.FIRST_NAME, nonStudent.getFirstName())
                                .setString(ClientConsts.LAST_NAME, nonStudent.getLastName())
                                .setInt(ClientConsts.MAX_BOOKS, nonStudent.getMaxBooks())
                                .setInt(ClientConsts.MAX_RENT_DAYS, nonStudent.getMaxRentDays());

                    }
                    case "Student" -> {
                        Student student = (Student) client;
                        yield session.prepare(studentHelper.updateByPrimaryKey().build()).bind()
                                .setUuid("id", student.getId())
                                .setString(ClientConsts.FIRST_NAME, student.getFirstName())
                                .setString(ClientConsts.LAST_NAME, student.getLastName())
                                .setString(ClientConsts.DISCRIMINATOR, student.getDiscriminator())
                                .setInt(ClientConsts.MAX_BOOKS, student.getMaxBooks())
                                .setInt(ClientConsts.MAX_RENT_DAYS, student.getMaxRentDays());
                    }
                    default -> throw new IllegalArgumentException();
                });
    }

    private NonStudent getNonStudent(Row nonStudent) {
        return new NonStudent(
                nonStudent.getUuid(ClientConsts.ID),
                nonStudent.getString(ClientConsts.DISCRIMINATOR),
                nonStudent.getFloat(ClientConsts.ADDITIONAL_FEE),
                nonStudent.getString(ClientConsts.FIRST_NAME),
                nonStudent.getString(ClientConsts.LAST_NAME),
                nonStudent.getInt(ClientConsts.MAX_BOOKS),
                nonStudent.getInt(ClientConsts.MAX_RENT_DAYS)
        );
    }

    private Student getStudent(Row student) {
        return new Student(
                student.getUuid(ClientConsts.ID),
                student.getString(ClientConsts.FIRST_NAME),
                student.getString(ClientConsts.LAST_NAME),
                student.getString(ClientConsts.DISCRIMINATOR),
                student.getInt(ClientConsts.MAX_BOOKS),
                student.getInt(ClientConsts.MAX_RENT_DAYS)
        );
    }
}
