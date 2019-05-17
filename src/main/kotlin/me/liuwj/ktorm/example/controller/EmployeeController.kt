package me.liuwj.ktorm.example.controller

import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.*
import me.liuwj.ktorm.example.dao.Employees
import me.liuwj.ktorm.example.model.Employee
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by vince on May 17, 2019.
 */
@RestController
class EmployeeController {

    @GetMapping("/employees/get-by-id")
    fun getEmployeeById(@RequestParam("id") id: Int): Employee? {
        return Employees.findById(id)
    }

    data class Page<T>(
        val offset: Int,
        val limit: Int,
        val totalRecords: Int,
        val pageSize: Int = limit,
        val totalPages: Int = if (totalRecords % pageSize == 0) totalRecords / pageSize else totalRecords / pageSize + 1,
        val currentList: List<T>
    )

    @GetMapping("/employees/get-list")
    fun getEmployees(
        @RequestParam("departmentId") departmentId: Int,
        @RequestParam("offset") offset: Int,
        @RequestParam("limit") limit: Int
    ): Page<Employee> {
        val employees = Employees
            .asSequence()
            .filter { it.departmentId eq departmentId }
            .drop(offset)
            .take(limit)

        return Page(offset, limit, employees.totalRecords, currentList = employees.toList())
    }

    @GetMapping("/employees/average-salaries")
    fun getAverageSalaries(): Map<Int?, Double?> {
        return Employees
            .asSequence()
            .groupingBy { it.departmentId }
            .eachAverageBy { it.salary }
    }
}